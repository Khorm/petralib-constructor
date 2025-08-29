import { useEffect, useRef } from 'react';

export const useReduxInitEffect = (data, callback) => {
  const isInitialized = useRef(false);

  useEffect(() => {
    if (data !== undefined && data !== null && !isInitialized.current) {
      isInitialized.current = true;
      callback(data);
    }
  }, [data, callback]);
};