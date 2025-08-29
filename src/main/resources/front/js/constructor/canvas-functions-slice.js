import { createSlice } from '@reduxjs/toolkit'

export const canvasFunctionsSlice = createSlice({
  name: 'canvasFunctions',
  initialState: {
    addBlock: undefined,
    removeBlock: undefined,
  },
  reducers: {

    setAdd: (state, action) => {
        console.log("set ADD")
        console.log(action)
        state.addBlock = action.payload;
    },

    setRemove: (state, action) => {
        console.log("set REMOVE")
        console.log(action)
        state.removeBlock = action.payload;
    },

  },
})

export const { setAdd, setRemove } = canvasFunctionsSlice.actions

export default canvasFunctionsSlice.reducer