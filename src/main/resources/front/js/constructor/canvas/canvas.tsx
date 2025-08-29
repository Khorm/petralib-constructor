import React, { useState, useEffect, useMemo, useRef } from 'react'

import axios from 'axios';

import './canvas.css';

import createEngine, { DiagramModel, DefaultNodeModel, DefaultLinkModel } from '@projectstorm/react-diagrams';

import { CanvasWidget } from '@projectstorm/react-canvas-core';
import { useSelector, useDispatch } from 'react-redux';

import ScenarioBlockModal from './modal/scenario-block-modal';
import WorkflowExitModal from './modal/workflow/workflow-exit-modal';




export default function Canvas() {

    const workflow = useSelector((state) => state.workflow.value);
    const addingBlock = useSelector((state) => state.canvasFunctions.addBlock);
    const engine = useMemo(() => {
        const engine = createEngine();
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
                            selectionChanged: (e) => choose(e)
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
                            entityRemoved: removeLink,
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

    function removeLink(e) {

        if (e.entity.sourceNode === undefined || e.entity.targetNode === undefined) return;
        let sourceBlock = e.entity.sourceNode.options.block;
        let targetBlock = e.entity.targetNode.options.block;
        let beginEndSourceBlock = e.entity.sourceNode.options.beginEndBlock;
        let beginEndTargetBlock = e.entity.targetNode.options.beginEndBlock;

        if (sourceBlock) sourceBlock.nextBlock = null;
        if (targetBlock) targetBlock.previousBlock = null;
        if (beginEndSourceBlock) beginEndSourceBlock.connectedBlockId = null;
        if (beginEndTargetBlock) beginEndTargetBlock.connectedBlockId = null;


    }

    function editLinks(e) {

        if (e.isCreated) {
            e.link.registerListener({
                entityRemoved: removeLink,
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
        if (!block) return;
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

        const node = new DefaultNodeModel({
            name: block.name,
            color: blockColor,
            block: block
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
        console.log("CHOOSING ,", chosenBlock.current);

        if (!e.isSelected) return;
        chosenBlock.current = e.entity.options;
    }


    function openMod() {
        save(() => {
            if (!openModal) {
                setOpenModal(true);
            } else {
                setOpenModal(false);
            }
        })

    }


    return (
        <>
            <div>
                <button style={{ width: '100%', height: '50%' }} onClick={openMod}>edit</button>
                <button style={{ width: '100%', height: '50%' }} onClick={save} >save</button>
            </div>
            <CanvasWidget className="diagram-container" engine={engine} />
            {openModal && chosenBlock.current.block &&
                <ScenarioBlockModal scenarioBlock={chosenBlock.current} workflow={prevWorkflowRef.current}
                    open={openModal} handleClose={openMod} />
            }

            {openModal && chosenBlock.current.beginEndBlock &&
                <WorkflowExitModal workflow={prevWorkflowRef.current}
                    open={openModal} handleClose={openMod} />
            }
        </>
    );

}
