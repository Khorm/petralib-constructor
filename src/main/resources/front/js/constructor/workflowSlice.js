import { createSlice } from '@reduxjs/toolkit'

export const workflowSlice = createSlice({
  name: 'workflow',
  initialState: {
    value: undefined,
  },
  reducers: {

    chooseWorkflow: (state, action) => {
        console.log("set Workflow")
        console.log(action)
        state.value = action.payload;
    },

  },
})

export const { chooseWorkflow } = workflowSlice.actions

export default workflowSlice.reducer