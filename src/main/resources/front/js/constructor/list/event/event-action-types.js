export const EVENTS = 'EVENTS';


export function eventsList (events){
    return {
        type: EVENTS,
        events: events,
    }
}