package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.ChatManagementService;
import com.martzatech.vdhg.crmprojectback.application.services.FileManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatOutOfficeMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatOutOffice;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageOutOfficeService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatMessageSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatOutOfficeSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomv1Specification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomv2Specification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.ChatApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.ChatMessageApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.ChatMessageFileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.ChatRoomApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatMessageRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatRoomRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OutOfficeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatMessageResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatOutOfficeResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatRoomResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chats")
@Validated
public class ChatController implements ChatApi {

    private final ChatManagementService service;
    private final ChatRoomApiMapper chatRoomApiMapper;
    private final ChatMessageApiMapper chatMessageApiMapper;
    private final ChatMessageFileApiMapper chatMessageFileApiMapper;
    private final ChatOutOfficeMapper chatOutOfficeMapper;
    private final ChatMessageOutOfficeService chatMessageOutOfficeService;
    private final FileManagementService fileManagementService;
    private final FileMapper fileMapper;
    @Override
    public ChatRoomResponse save(final ChatRoomRequest request) {
        return chatRoomApiMapper.modelToResponse(service.saveRoom(chatRoomApiMapper.requestToModel(request)));
    }

    @Override
    public PaginatedResponse<ChatRoomResponse> findAll(final ChatRoomv1Specification specification,
                                                       final Integer page,
                                                       final Integer size, final String direction, final String attribute, final String type) {
        ChatRoomv1Specification chatRoomv1Specification = new ChatRoomv1Specification(type, specification.getAccountName(),
                specification.getMembership(), specification.getMembers(), specification.getMembersName(), specification.getId(), specification.getName(),
             specification.getUnRead(), specification.getPriority());

        final List<ChatRoom> response = service.findAll(chatRoomv1Specification, page, size, direction, attribute);

        final Long total = service.countChatRoom(specification);
        return PaginatedResponse.<ChatRoomResponse>builder()
                .total(total.intValue())
                .page(page)
                .size(response.size())
                .items(chatRoomApiMapper.modelsToResponseList(response))
                .build();
    }


    @Override
    public ChatMessageResponse saveFile(final Integer chatId, final MultipartFile file, final String extension,
                                        final String text,final String value) {
        return chatMessageApiMapper.modelToResponse(service.saveFile(chatId, file, extension,text,value));
    }

    @Override
    public ChatMessageResponse saveFileAndUpdate(Integer chatId,Integer messageId, Integer fileId, MultipartFile file, String extension) {
        return chatMessageApiMapper.modelToResponse(fileManagementService
                .saveFileChatRoom( chatId,messageId,  fileId,  file,  extension));
    }

    @Override
    public List<ChatMessageResponse> saveMessage(final Integer chatId, final ChatMessageRequest request) throws ParseException {
        return chatMessageApiMapper
                .modelsToResponseList(service.saveMessage(chatId, chatMessageApiMapper.requestToModel(request)));
    }

    @Override
    public PaginatedResponse<ChatMessageResponse> findAllMessages(final ChatMessageSpecification specification,
                                                                  final Integer chatId, final Integer page,
                                                                  final Integer size, final String direction, final String attribute) {
        final long total = service.countMessages(chatId, specification);
        final List<ChatMessage> response = service.findAllMessages(chatId, specification, page, size, direction, attribute);
        service.readMessage(chatId);
        return PaginatedResponse.<ChatMessageResponse>builder()
                .total((int) total)
                .page(page)
                .size(response.size())
                .items(chatMessageApiMapper.modelsToResponseList(response))
                .build();
    }

    @Override
    public PaginatedResponse<ChatMessageResponse>   findAllChatMessages(final ChatMessageSpecification specification,
                                                                      final Integer page,
                                                                      final Integer size, final String direction,
                                                                      final String attribute, String value,
                                                                      final String advanceSearch) {
        final List<ChatMessage> response = service.findAllChatMessages(specification, page, size, direction, attribute, value, advanceSearch);
        response.forEach(chatMessage -> service.readMessage(chatMessage.getChatRoom().getId()));
        final long total = StringUtils.isBlank(value) || StringUtils.isEmpty(value)
                ? service.countChatMessages(specification) : response.size();
        return PaginatedResponse.<ChatMessageResponse>builder()
                .total((int) total)
                .page(page)
                .size(response.size())
                .items(chatMessageApiMapper.modelsToResponseList(response))
                .build();
    }

    @Override
    public void deleteById(final Integer id) {
        service.deleteById(id);
    }

    @Override
    public void deleteChatMessageFile(Integer messageId,Integer fileId) {
        fileManagementService.validId(fileId);
        service.deleteChatMessageById(messageId);
        fileManagementService.findAllMessageFile(messageId).forEach(file ->
                fileManagementService.deleteMessageFile(fileMapper.modelToEntity(file)));
    }

    @Override
    public ChatOutOfficeResponse findById(final Integer id) {
        return chatOutOfficeMapper.modelToResponse(chatMessageOutOfficeService.findById(id));
    }

    @Override
    public boolean findByLastMessage() {
        return service.findLastMessage();
    }


    @Override
    public ChatOutOfficeResponse saveOutOfOffice(final OutOfficeRequest request) {
        return chatOutOfficeMapper.modelToResponse(service.saveOutOffice(chatOutOfficeMapper.requestToModel(request)));
    }

    public PaginatedResponse<ChatOutOfficeResponse> findAllOutOfOffice(final ChatOutOfficeSpecification specification,
                                                                       final Integer page, final Integer size,
                                                                       final String direction, final String attribute) {
        final Long total = service.count(specification);
        final List<ChatOutOffice> response = service
                .findAllOutOffice(specification, page, size, direction, attribute);

        return PaginatedResponse.<ChatOutOfficeResponse>builder()
                .total(total.intValue())
                .page(page)
                .size(response.size())
                .items(chatOutOfficeMapper.modelsToResponseList(response))
                .build();
    }

    @Override
    public void readMessage(final Integer chatId) {
        service.readMessage(chatId);
    }

    @Override
    public void archiveMessage(final Integer chatId) {
        service.archiveMessage(chatId);
    }


    @Override
    public List<ChatMessageResponse> findUnread(final Integer chatId, final Integer page, final Integer size) {
        service.readMessage(chatId);
        return chatMessageApiMapper.modelsToResponseList(service.findUnread(chatId, page, size));
    }

    @Override
    public PaginatedResponse<ChatRoomResponse> findAllChatArchive(ChatRoomv2Specification specification, Integer page, Integer size, String direction, String attribute) {
        final List<ChatRoom> response = service.findAllChatArchive(specification, page, size, direction, attribute);
        final Long total = service.countChatRoomArchive(specification);
        return PaginatedResponse.<ChatRoomResponse>builder()
                .total(total.intValue())
                .page(page)
                .size(response.size())
                .items(chatRoomApiMapper.modelsToResponseList(response))
                .build();
    }

    @Override
    public List<ChatMessageResponse> findLast(final Integer chatId, final Integer messageId, final Integer page,
                                              final Integer size) {
        service.readMessage(chatId);
        return chatMessageApiMapper.modelsToResponseList(service.findLast(chatId, messageId, page, size));
    }

    @Override
    public List<ChatMessageResponse> findNext(final Integer chatId, final Integer messageId, final Integer page,
                                              final Integer size) {
        service.readMessage(chatId);
        return chatMessageApiMapper.modelsToResponseList(service.findNext(chatId, messageId, page, size));
    }
}
