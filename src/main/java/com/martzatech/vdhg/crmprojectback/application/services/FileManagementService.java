package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class FileManagementService {

    private final FileService service;
    private final SecurityManagementService securityManagementService;
    private final ChatRoomService roomService;
    private final CustomerService customerService;
    private final AzureFileService azureFileService;
    private final ChatMessageService chatMessageService;
    private final FileApiMapper fileApiMapper;
    private static final String AZURE_PATH_CHAT = "chats";
    @Transactional
    public ChatMessage saveFileChatRoom(Integer chatId,Integer messageId, Integer fileId, MultipartFile file, String extension) {
        Customer customer = null;
        final User currentUser = securityManagementService.findCurrentUser();
        validateChatRoom(chatId);
        validId(fileId);
        ChatMessage chatMessage = chatMessageService.findById(messageId);
        ChatRoom chatRoom = roomService.findById(chatId);
        if (Objects.nonNull(chatRoom.getCustomer()) && !chatRoom.getType().equals(ChatRoomTypeEnum.INTERNAL)) {
            customer = customerService.findById(chatRoom.getCustomer().getId());
        }
        final String url = azureFileService.uploadFile(file, AZURE_PATH_CHAT, extension);
        File fileSave = service.save(buildFileSave(getModel(file.getOriginalFilename(),
                fileId,extension, url, customer, currentUser), currentUser));
        return  chatMessage.withFiles(List.of(fileApiMapper.modelToResponse(fileSave)));
    }

    private File getModel(String nameFile,final Integer fileId,
                          final String extension, final String url,
                          Customer customer, User currentUser) {
        return File.builder()
                .id(Objects.nonNull(fileId)? fileId:null)
                .url(url)
                .extension(extension)
                .name(StringUtils.isNotBlank(nameFile) ? nameFile : getNameUrlPdf(url))
                .persons(Objects.nonNull(customer) ? List.of(customer.getPerson()) : null)
                .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                .creationUser(Objects.isNull(currentUser) ? securityManagementService.findCurrentUser() : currentUser)
                .creationTime(LocalDateTime.now())
                .build();
    }

    private static String getNameUrlPdf(String url) {
        String uuid = "";
        // Encuentra la posición del último "/" y ".pdf" en la URL
        int lastSlashIndex = url.lastIndexOf("/");
        int pdfIndex = url.indexOf(".pdf");

        // Extrae el UUID de la URL
        if (lastSlashIndex != -1 && pdfIndex != -1) {
            uuid = url.substring(lastSlashIndex + 1, pdfIndex);

        }
        return uuid;
    }

    public List<File> findAllMessageFile(final Integer messageId){
        return service.findAllChatMessageFile(messageId);
    }

    private void validateChatRoom(Integer chatId) {
        if (Objects.nonNull(chatId)) {
            roomService.existsById(chatId);
        }
    }

    public void validId(final Integer id) {
        if (Objects.nonNull(id)) {
            service.existsById(id);
        }
    }

    public void deleteMessageFile(FileEntity entity){
        service.deleteById(entity.getId());
    }

    public File buildFileSave(final File model, User userCurrent) {
        final File fromDDBB = Objects.nonNull(model.getId())
                ? service.findById(model.getId())
                .withExtension(model.getExtension())
                .withName(model.getName())
                .withRemovalTime(model.getRemovalTime())
                .withCreationTime(model.getCreationTime())
                .withUrl(model.getUrl())
                .withPersons(Objects.nonNull(model.getPersons()) ? model.getPersons() : null)
                .withStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                .withCreationUser(model.getCreationUser())
                .withRemovalUser(userCurrent)
                : service.save(model.withCreationUser(userCurrent));
        return fromDDBB
                .withExtension(model.getExtension())
                .withName(model.getName())
                .withRemovalTime(model.getRemovalTime())
                .withCreationTime(model.getCreationTime())
                .withUrl(model.getUrl())
                .withPersons(Objects.nonNull(model.getPersons()) ? model.getPersons() : null)
                .withStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                .withCreationUser(userCurrent)
                .withRemovalUser(userCurrent);
    }


}
