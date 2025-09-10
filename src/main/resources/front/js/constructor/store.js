import { configureStore } from '@reduxjs/toolkit'
import workflowReducer from './workflowSlice'
import canvasFunctionsSlice from './canvas-functions-slice'
import scenarioVariablesSlice from './canvas/modal/scenario-variable-slice'
import localIdSlice from './canvas/modal/selector/id/local-id-slice'

export default configureStore({
  reducer: {
    workflow: workflowReducer,
    canvasFunctions: canvasFunctionsSlice,
    scenarioVariables: scenarioVariablesSlice,
    localId: localIdSlice,
  },
})