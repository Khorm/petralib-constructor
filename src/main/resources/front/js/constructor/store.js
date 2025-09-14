import { configureStore } from '@reduxjs/toolkit'
import workflowReducer from './workflowSlice'
import canvasFunctionsSlice from './canvas-functions-slice'
import scenarioVariablesSlice from './canvas/modal/scenario-variable-slice'

export default configureStore({
  reducer: {
    workflow: workflowReducer,
    canvasFunctions: canvasFunctionsSlice,
    scenarioVariables: scenarioVariablesSlice,    
  },
})