import React from 'react';
import {Glyphicon} from 'react-bootstrap';
import './cardcreator.css';
import {connect} from 'react-redux';
import {updateFieldMerge as updateFolderFieldMerge, clearData as clearFolderData, loadData as loadFolderData} from '@/store/adminCardFolderEdit.store.js';
import {updateFieldMerge as updateCardFieldMerge, clearData as clearCardData, loadData as loadCardData} from '@/store/adminCardCreatorEdit.store.js';

class CardFolderClass extends React.Component {
    constructor(props) {
        super(props);
        this.open = this.open.bind(this);
        this.createFolder = this.createFolder.bind(this);
        this.createCard = this.createCard.bind(this);
        this.editFolder = this.editFolder.bind(this);
        this.state = {open: false};
    }
    open(e) {
        e.stopPropagation();
        this.setState({open: !this.state.open});
    }
    canCreateFolder(elem) {
        return !elem.get('finalized');
        // return !elem.get('cards') || elem.get('cards').size == 0;
    }
    canCreateCard(elem) {
        return !elem.get('finalized');
        // return !elem.get('children') || elem.get('children').size == 0;
    }
    createCard(e) {
        e.stopPropagation();
        this.props.startCardCreation();
    }
    createFolder(e) {
        e.stopPropagation();
        this.props.startFolderCreation();
    }
    editCard(event, elem) {
        event.stopPropagation();
        this.props.startCardEdit(elem.get('id'));
    }
    editFolder(event) {
        event.stopPropagation();
        this.props.startFolderEdit(this.props.elem.get('id'));
    }
    render() {
        return(
      <div className={`card-folder ${this.state.open ? 'card-folder-open' : ''}`}>
        <div onClick={this.open}>
          <Glyphicon glyph={`${this.state.open ? 'folder-open' : 'folder-close'}`}/>
          <span>{this.props.elem.get('name')}</span>
          <Glyphicon className="card-folder-button card-folder-edit" glyph="pencil" title="Редактировать" onClick={this.editFolder}/>
          {this.canCreateFolder(this.props.elem) &&
            <Glyphicon className="card-folder-button card-folder-new-folder" glyph="inbox" title="Создать папку" onClick={this.createFolder}/>
          }
          {this.canCreateCard(this.props.elem) &&
            <Glyphicon className="card-folder-button card-folder-new-card" glyph="plus" title="Создать карточку" onClick={this.createCard}/>
          }
        </div>
        {this.state.open && this.props.elem.get('children') &&
          this.props.elem.get('children').filter(e=>!e.get('hidden')).map(e=><CardFolder key={e.get('id')} elem={e}/>)
        }
        {this.state.open && this.props.elem.get('cards') &&
          this.props.elem.get('cards').filter(e=>!e.get('hidden')).map(e=><div className="card-folder" onClick={event=>this.editCard(event, e)}>
            <Glyphicon glyph="list-alt"/>{e.get('name')}
          </div>)
        }
      </div>
    );
    }
}
CardFolderClass .propTypes = {
    elem: React.PropTypes.object,
    startFolderCreation: React.PropTypes.func,
    startCardCreation: React.PropTypes.func,
    startCardEdit: React.PropTypes.func,
    startFolderEdit: React.PropTypes.func,
};
const CardFolder = connect(()=>({}), (dispatch, props)=>({
    startFolderCreation() {
        clearFolderData(dispatch);
        clearCardData(dispatch);
        updateFolderFieldMerge(dispatch, {editMode: true, parent: props.elem.get('id')});
    },
    startFolderEdit(id) {
        clearFolderData(dispatch);
        clearCardData(dispatch);
        loadFolderData(dispatch, id);
    },
    startCardCreation() {
        clearFolderData(dispatch);
        clearCardData(dispatch);
        updateCardFieldMerge(dispatch, {editMode: true, folder: props.elem.get('id')});
    },
    startCardEdit(id) {
        clearFolderData(dispatch);
        clearCardData(dispatch);
        loadCardData(dispatch, id);
    }
}))(CardFolderClass);

export default CardFolder;
