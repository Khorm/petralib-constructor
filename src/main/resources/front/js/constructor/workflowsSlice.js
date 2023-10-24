import { createSlice } from '@reduxjs/toolkit'

export const workflowsSlice = createSlice({
  name: 'workflows',
  initialState: {
    value: [],
  },
  reducers: {

    set: (state, action) => {
        console.log("set Workflows tree")
        console.log(action)
        if (action.payload === undefined) action.payload = []
        let metaWorkflows = action.payload.map((workflow) => {
            return {
                workflow: workflow,
                children: [],
                childrenLoaded: false
            }
        });
        state.value = metaWorkflows;
    },


    update: (state, action) => {
        console.log("update Workflows tree")
        console.log(action)
        if (action.payload === undefined) action.payload = []
        state.value = state.value.map((metaWorkflow) => {
            if (metaWorkflow.workflow.id !== action.payload.workflow.id){
                return metaWorkflow;
            }
            return action.payload;
        })
    }
  },
})

export const { set, update } = workflowsSlice.actions

export default workflowsSlice.reducer