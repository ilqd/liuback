/* eslint new-cap: ["error", { "capIsNew": false }]*/
import { Map, List, fromJS } from 'immutable';
import {RestAPI} from '@/net.js';
import {SUCCESS_MESSAGE} from './net.store';
import {commonLoadData} from './commonFunctions';

export const adminPageEditReducer = (state = Map(), action) => {
    switch (action.type) {
        case 'PAGE_TO_EDIT_LOADED':
            return fromJS(action.data);
        case 'CHANGE_PAGE_FIELD':
            return state.set(action.field, action.value);
        case 'PAGE_ADD_ROW':
            return state.setIn(action.path,
              state.getIn(action.path, new List()).push(new Map({type: 'ROW'}))
            );
        case 'LOGOUT':
        case 'CREATING_NEW_PAGE':
            return Map();
        default:
            return state;
    }
};

export const createNewPage = (dispatch) =>{
    dispatch({ type: 'CREATING_NEW_PAGE' });
};
export const loadPageToEdit = (dispatch, id) => commonLoadData(dispatch, `/api/page/${id}`,
    'LOADING_PAGE_TO_EDIT', 'PAGE_TO_EDIT_LOADED', 'PAGE_TO_EDIT_LOAD_FAILED');

export const changeField = (dispatch, field, value) =>{
    dispatch({ type: 'CHANGE_PAGE_FIELD', field, value });
};

export const addRow = (dispatch, path)=>{
    dispatch({type: 'PAGE_ADD_ROW', path});
};


export const save = (dispatch, data) =>{
    dispatch({type: 'SAVING_PAGE'});
    dispatch({ type: 'POSTING' });
    RestAPI.put('/api/page/savePage', data).then(
      () => {
          dispatch({ type: 'POSTED', message: SUCCESS_MESSAGE});
      }
    ).catch(
      (response) => {
          dispatch({ type: 'POSTED', message: `Ошибка! :( Сервер жалуется на это:   ${response.message}.` });
      }
    );
};
