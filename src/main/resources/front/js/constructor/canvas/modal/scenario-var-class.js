import _ from 'lodash';

export default class ScenarioVariable{
    #scenarioVariableId;
    #type;
    #consumerVariableId;
    #producerVariableId;
    #sourceId;
    #typeInheritance;
    #script;

    constructor(data){
        this.#scenarioVariableId = data.scenarioVariableId;
        this.#type = data.type;
        this.#consumerVariableId = data.consumerVariableId;
        this.#producerVariableId = data.producerVariableId;
        this.#sourceId = data.sourceId;
        this.#typeInheritance = data.typeInheritance;
        this.#script = data.script;
    }

    deepCopy(){
        return _.cloneDeep(this);
    }


    set sourceId(newSourceId){
        this.#sourceId = newSourceId;
        this.#typeInheritance = undefined;
        this.#producerVariableId = undefined;
    }

    get sourceId() {
        return this.#sourceId;
    }

    set typeInheritance(newInh) {
        this.#typeInheritance = newInh;
    }

    get typeInheritance() {
        return this.#typeInheritance;
    }

    get consumerVariableId() {
        return this.#consumerVariableId;
    }

    get producerVariableId() {
        return this.#producerVariableId;
    }

    set producerVariableId(producerVariableId) {
        this.#producerVariableId = producerVariableId;
    }

    get type(){
        return this.#type;
    }

    get script(){
        return this.#script;
    }

    set script(newScript){
        this.#script = newScript;
    }

    getData(){
        return {
                //...this.#scenarioVariableId !== undefined && { scenarioVariableId: this.#scenarioVariableId },
                ...this.#type !== undefined && { type: this.#type },
                ...this.#consumerVariableId !== undefined && { consumerVariableId: this.#consumerVariableId },
                ...this.#producerVariableId !== undefined && { producerVariableId: this.#producerVariableId },
                ...this.#sourceId !== undefined && { sourceId: this.#sourceId },
                ...this.#typeInheritance !== undefined && { typeInheritance: this.#typeInheritance },
                ...this.#script !== undefined && { script: this.#script },
        }
    }

}