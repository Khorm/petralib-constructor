import {EVENTS} from './event-action-types'

const initialState = {
    events: new Map(),

}



export const eventReducer = function(state = initialState, action) {
    switch(action.type){

          case EVENTS:
              let events = Object.assign({}, state, {events: action.events});
              return events;
    }

    return state;
}