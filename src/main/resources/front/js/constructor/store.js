import { configureStore } from '@reduxjs/toolkit'
import workflowsReducer from './workflowsSlice'

export default configureStore({
  reducer: {
    workflows: workflowsReducer
  },
})