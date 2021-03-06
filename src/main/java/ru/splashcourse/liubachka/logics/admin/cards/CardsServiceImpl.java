package ru.splashcourse.liubachka.logics.admin.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import ru.splashcourse.liubachka.configs.orika.OrikaBeanMapper;
import ru.splashcourse.liubachka.logics.admin.cards.model.CardFolder;
import ru.splashcourse.liubachka.logics.admin.cards.model.CardFolderDto;
import ru.splashcourse.liubachka.logics.admin.cards.model.CardFolderRepository;
import ru.splashcourse.liubachka.logics.admin.cards.model.CardItem;
import ru.splashcourse.liubachka.logics.admin.cards.model.CardItemDto;
import ru.splashcourse.liubachka.logics.admin.cards.model.CardItemRepository;

@Service
@Transactional
public class CardsServiceImpl implements CardsService {

    @Autowired
    private CardFolderRepository folderRepo;

    @Autowired
    private CardItemRepository itemRepo;

    @Autowired
    private OrikaBeanMapper mapper;

    @Override
    public void createFolder(CardFolderDto dto) {
        dto.setId(null);
        CardFolder folder = mapper.map(dto, CardFolder.class);
        folderRepo.save(folder);
    }

    @Override
    public void updateFolder(CardFolderDto dto) {
        CardFolder folder = folderRepo.findOne(dto.getId()).get();
        folder.setName(dto.getName());
        folder.setHidden(dto.getHidden());
        folder.setFinalized(dto.getFinalized());
    }

    @Override
    public void deleteFolder(Long id) {
        folderRepo.getOne(id).setHidden(true);
    }

    @Override
    public void createCard(CardItemDto dto) {
        dto.setId(null);
        CardItem item = mapper.map(dto, CardItem.class);
        itemRepo.save(item);
    }

    @Override
    public void updateCard(CardItemDto dto) {
        CardItem item = itemRepo.findOne(dto.getId()).get();
        mapper.map(dto, item);
    }

    @Override
    public void deleteCard(Long id) {
        itemRepo.getOne(id).setHidden(true);
    }

    @Override
    public List<CardFolderDto> getAll() {
        List<CardFolderDto> result = new ArrayList<>();
        mapper.mapAsCollection(folderRepo.findByParentIsNull(), result, CardFolderDto.class);
        ;
        return result;
    }

    @Override
    public CardFolderDto getFolder(Long id) {
        return mapper.map(folderRepo.getOne(id), CardFolderDto.class);
    }

    @Override
    public CardItemDto getCard(Long id) {
        return mapper.map(itemRepo.getOne(id), CardItemDto.class);
    }

    @Override
    public void restoreFolder(Long id) {
        folderRepo.getOne(id).setHidden(false);
        ;
    }

    @Override
    public void restoreCard(Long id) {
        itemRepo.getOne(id).setHidden(false);
    }
}
