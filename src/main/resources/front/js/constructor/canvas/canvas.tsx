import React, { useState, useEffect, useMemo, useRef } from 'react'
import _forEach from 'lodash/forEach';

import axios from 'axios';

import './canvas.css';

import createEngine, { DiagramModel, DefaultNodeModel, DefaultLinkModel } from '@projectstorm/react-diagrams';

import { CanvasWidget } from '@projectstorm/react-canvas-core';
import { useSelector, useDispatch } from 'react-redux';

import ScenarioBlockModal from './modal/scenario-block-modal';
import WorkflowExitModal from './modal/workflow/workflow-exit-modal';
import CustomDeleteItemsAction from './CustomDeleteItemsAction'




export default function Canvas() {

    const workflow = useSelector((state) => state.workflow.value);
    const addingBlock = useSelector((state) => state.canvasFunctions.addBlock);
    const engine = useMemo(() => {
        const engine = createEngine({ registerDefaultDeleteItemsAction: false });
        engine.setModel(new DiagramModel()); // ������������� ������
        engine.maxNumberPointsPerLink = 0;
        return engine;
    }, []);
    const dispatch = useDispatch();
    const prevWorkflowRef = useRef();
    const chosenBlock = useRef();

    const [openModal, setOpenModal] = useState(false);


    useEffect(() => {
        addBlock(addingBlock);
    }, [addingBlock])


    useEffect(() => {
        console.log("UPDATE WORKFLOW ", workflow);
        if (workflow !== undefined) {
            if (prevWorkflowRef.current !== undefined) {
                save();
            }
            prevWorkflowRef.current = workflow;

            axios.get('/api/v1/scenario', {
                params: {
                    workflowId: workflow,
                }
            })
                .then((response) => {
                    let blocks = response.data.scenarioBlocks;
                    let beginEnd = response.data.beginEndDtoList;
                    console.log('response.data', response.data)
                    const model = new DiagramModel();

                    model.registerListener({
                        linksUpdated: editLinks
                    });

                    engine.setModel(model);
                    let dict = {};
                    blocks.forEach((block) => {
                        let blockColor;
                        if (block.type === 'ACTION') {
                            blockColor = 'LightGreen';
                        } else {
                            blockColor = 'PowderBlue';
                        }

                        let node = new DefaultNodeModel({
                            name: block.name,
                            color: blockColor,
                            block: block,
                        });
                        dict[block.id] = node;
                        node.setPosition(block.x, block.y);
                        const portOut = node.addOutPort('Out');
                        const portIn = node.addInPort("In");

                        engine.getModel().addNode(node);
                        node.registerListener({
                            selectionChanged: (e) => choose(e),

                        });
                    });

                    blocks.forEach((block) => {
                        if (block.nextBlock === null) {
                            return;
                        }
                        let link = new DefaultLinkModel();

                        link.setSourcePort(dict[block.id].getPort("Out"));
                        link.setTargetPort(dict[block.nextBlock].getPort("In"));
                        link.sourceNode = dict[block.id];
                        link.targetNode = dict[block.nextBlock];
                        link.registerListener({
                            entityRemoved: removeLink,
                        })

                        engine.getModel().addLink(link);
                    });

                    beginEnd.forEach((beginEndBlock) => {
                        let node = new DefaultNodeModel({
                            name: beginEndBlock.pointType,
                            color: 'gray',
                            beginEndBlock: beginEndBlock,
                        });
                        node.setPosition(beginEndBlock.x, beginEndBlock.y);
                        engine.getModel().addNode(node);

                        let link = new DefaultLinkModel();
                        link.registerListener({
                            entityRemoved: (e) => removeLink(e.entity),
                        })

                        if (beginEndBlock.pointType === 'START') {
                            const portOut = node.addOutPort('Out');

                            if (beginEndBlock.connectedBlockId === null) return;
                            link.setSourcePort(portOut);
                            link.setTargetPort(dict[beginEndBlock.connectedBlockId].getPort("In"));
                            link.sourceNode = beginEndBlock.id;
                            link.targetNode = dict[beginEndBlock.connectedBlockId];
                            engine.getModel().addLink(link);

                        } else {
                            const portIn = node.addInPort("In");
                            console.log('EXIT ', node);
                            node.registerListener({
                                selectionChanged: (e) => choose(e)
                            });

                            if (beginEndBlock.connectedBlockId === null) return;
                            link.setTargetPort(portIn);
                            link.setSourcePort(dict[beginEndBlock.connectedBlockId].getPort("Out"));
                            link.sourceNode = beginEndBlock.id;
                            link.targetNode = dict[beginEndBlock.connectedBlockId];
                            engine.getModel().addLink(link);
                        }
                    })

                    engine.repaintCanvas();

                });
        }

    }, [workflow]);

    function removeLink(entity) {

        if (entity.sourceNode === undefined || entity.targetNode === undefined) return;
        let sourceBlock = entity.sourceNode.options.block;
        let targetBlock = entity.targetNode.options.block;
        let beginEndSourceBlock = entity.sourceNode.options.beginEndBlock;
        let beginEndTargetBlock = entity.targetNode.options.beginEndBlock;

        if (sourceBlock) sourceBlock.nextBlock = null;
        if (targetBlock) targetBlock.previousBlock = null;
        if (beginEndSourceBlock) beginEndSourceBlock.connectedBlockId = null;
        if (beginEndTargetBlock) beginEndTargetBlock.connectedBlockId = null;

    }

    function editLinks(e) {

        if (e.isCreated) {
            e.link.registerListener({
                entityRemoved: (e) => removeLink(e.entity),
                targetPortChanged: targetPortUpdate
            })
            e.link.sourceNode = e.link.sourcePort.parent;

        }
    }

    function targetPortUpdate(e) {

        if (e.entity.targetPort.options.name != 'In') {
            engine.getModel().removeLink(e.entity);
            engine.repaintCanvas();
            return;
        }

        e.entity.targetNode = e.port.parent;

        let sourceBeginEndBlock = e.entity.sourcePort.parent.options.beginEndBlock;
        let targetBeginEndBlock = e.entity.targetPort.parent.options.beginEndBlock;
        let sourceBlock = e.entity.sourcePort.parent.options.block;
        let targetBlock = e.entity.targetPort.parent.options.block;


        if (sourceBeginEndBlock !== undefined && sourceBeginEndBlock.pointType === 'START') {
            sourceBeginEndBlock.connectedBlockId = targetBlock.id;
        } else if (targetBeginEndBlock !== undefined && targetBeginEndBlock.pointType === 'END') {
            targetBeginEndBlock.connectedBlockId = sourceBlock.id;
        } else {
            targetBlock.previousBlock = sourceBlock.id;
            sourceBlock.nextBlock = targetBlock.id;
        }

    }


    function save(callback = undefined) {

        let scenarioBlocks = [];
        let beginEndDtoList = [];
        const nodes = engine.getModel().getNodes();
        for (let i = 0; i < nodes.length; i++) {
            let block = nodes[i].options.block;
            let beginEndBlock = nodes[i].options.beginEndBlock;

            console.log("SAVE ", block);

            if (block) {
                scenarioBlocks.push({
                    id: block.id,
                    blockId: block.blockId,
                    name: block.name,
                    blockType: block.blockType,
                    x: nodes[i].position.x,
                    y: nodes[i].position.y,
                    previousBlock: block.previousBlock,
                    nextBlock: block.nextBlock,
                })
            } else if (beginEndBlock) {
                beginEndDtoList.push({
                    id: beginEndBlock.id,
                    x: nodes[i].position.x,
                    y: nodes[i].position.y,
                    pointType: beginEndBlock.pointType,
                    connectedBlockId: beginEndBlock.connectedBlockId
                })
            }
        }

        let scenario = {
            scenarioBlocks: scenarioBlocks,
            beginEndDtoList: beginEndDtoList
        }
        console.log("SAVED : ", scenario);

        axios.post('/api/v1/scenario', scenario, {
            params: {
                workflowId: prevWorkflowRef.current
            }
        }
        ).then((response) => {
            if (typeof callback === 'function') {
                callback();
            }
        }).catch((error) => {
            console.log(error);
            alert(error)
        })
    }


    function addBlock(block) {
        console.log("ADD", block, workflow);
        if (!block || !workflow || block.id === workflow) return;
        let allNodes = engine.getModel().getNodes();
        for (let i = 0; i < allNodes.length; i++) {
            if (allNodes[i].options.block && allNodes[i].options.block.id === block.id) {
                return;
            }
        }

        let blockColor;
        if (block.type === 'ACTION') {
            blockColor = 'LightGreen';
        } else {
            blockColor = 'PowderBlue';
        }

        const scenarioBlock = {
            blockId: block.id,
            name: block.name,
            blockType: block.type,

        }

        const node = new DefaultNodeModel({
            name: block.name,
            color: blockColor,
            block: scenarioBlock
        });
        node.setPosition(100, 100);
        const portOut = node.addOutPort('Out');
        const portIn = node.addInPort("In");
        engine.getModel().addNode(node);
        node.registerListener({
            selectionChanged: (e) => choose(e)
        });
        engine.repaintCanvas();
    }


    function choose(e) {
        if (!e.isSelected) return;
        chosenBlock.current = e.entity.options;
    }


    function openMod() {
        save(() => {
            if (chosenBlock.current === undefined) return;
            if (!openModal) {
                setOpenModal(true);
            } else {
                setOpenModal(false);
            }
        })

    }

    function deleteBlock() {
        const nodes = engine.getModel().getNodes();
        _forEach(nodes, (node) => {
            // console.log('DELET : ', node.options,  chosenBlock.current)
            if (node.options.block !== undefined && node.options.block.blockId === chosenBlock.current?.block.blockId) {
                engine.getModel().removeNode(node);
            }
        });
        
        const links = engine.getModel().getLinks();
        _forEach(links, (link) => {            
            if (link.sourceNode?.options.block !== undefined && link.sourceNode?.options.block.blockId === chosenBlock.current?.block.blockId){
                removeLink(link);
                engine.getModel().removeLink(link);                
            }
            if (link.targetNode?.options.block !== undefined && link.targetNode?.options.block.blockId === chosenBlock.current?.block.blockId){
                // console.log('DELET LINK: ', link,  chosenBlock.current);
                removeLink(link);
                engine.getModel().removeLink(link);                
            }
        });
        chosenBlock.current = undefined;
        engine.repaintCanvas();
    }


    return (
        <>
            <div>
                <button style={{ width: '100%', height: '33%' }} onClick={openMod}>edit</button>
                <button style={{ width: '100%', height: '33%' }} onClick={save} >save</button>
                <button style={{ width: '100%', height: '33%' }} onClick={deleteBlock} >delete</button>
            </div>
            <CanvasWidget className="diagram-container" engine={engine} />
            {openModal && chosenBlock.current.block &&
                <ScenarioBlockModal scenarioBlock={chosenBlock.current.block} workflow={prevWorkflowRef.current}
                    open={openModal} handleClose={openMod} />
            }

            {openModal && chosenBlock.current.beginEndBlock &&
                <WorkflowExitModal workflow={prevWorkflowRef.current.beginEndBlock}
                    open={openModal} handleClose={openMod} />
            }
        </>
    );

}
