import {RestAPI} from '@/net.js';
import{Map, fromJS} from 'immutable';
import {goBack, history} from '@/store/store';

export const useraccountReducer = (state = Map(), action) => {
    switch (action.type) {
        case 'LOGIN_SUCCESSFUL':
            return fromJS(action.info);
        case 'LOGIN_ATTEMPT_STARTED':
        case 'LOGOUT':
            return Map();
        case'LOGIN_FAILED':
            return Map().set('login_error', action.error);
        default:
            return state;
    }
};


export const tryToLogin = (dispatch, username, pass) => {
    dispatch({ type: 'LOGIN_ATTEMPT_STARTED' });
    return RestAPI.post('/login', { username, password: pass })
    .then((response) => {
        dispatch({ type: 'LOGIN_SUCCESSFUL',  info: {csrf: response.csrf, firstName: response.firstName,
        lastName: response.lastName, userId: response.userId,
         roles: response.roles} });
        goBack();
    },
      (response) => {
          dispatch({ type: 'LOGIN_FAILED', error: response.login_error });
      }
    );
};
export const logout = (dispatch)=>{
    dispatch({ type: 'LOGOUT' });
    RestAPI.post('/logout');
    history.replace('/login');
};
