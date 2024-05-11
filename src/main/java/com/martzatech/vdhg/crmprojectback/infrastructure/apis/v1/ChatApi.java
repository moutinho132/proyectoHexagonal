package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatMessageSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatOutOfficeSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomv1Specification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomv2Specification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatMessageRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatRoomRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OutOfficeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatMessageResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatOutOfficeResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatRoomResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface ChatApi {

    @Operation(summary = "Save a Chat Room")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ChatRoomResponse save(@RequestBody @Valid ChatRoomRequest request);

    @Operation(summary = "Get all Chat Rooms")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<ChatRoomResponse> findAll(final ChatRoomv1Specification specification,
                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute,
                                                @RequestParam(name = "type", defaultValue = "") String type);


    @Operation(summary = "Save a Message in a Chat Room Verify Out Office")
    @PostMapping(path = "{chatId}/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<ChatMessageResponse> saveMessage(@PathVariable("chatId") Integer chatId,
                                          @RequestBody @Valid ChatMessageRequest request) throws ParseException;

    @Operation(summary = "Save a File in a Chat Room")
    @PostMapping(path = "{chatId}/files")
    @ResponseStatus(HttpStatus.OK)
    ChatMessageResponse saveFile(@PathVariable("chatId") Integer chatId,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam("extension") String extension,
                                 @RequestParam(value = "text",required = false,defaultValue = "") String text,
                                 @RequestParam(value = "value",required = false,defaultValue = "") String value);

    @Operation(summary = "Save and update File in a Chat Room")
    @PostMapping(path = "{chatId}/message/{messageId}/files/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    ChatMessageResponse saveFileAndUpdate(@PathVariable("chatId") Integer chatId,
                                          @PathVariable("messageId") Integer messageId,
                                          @PathVariable("fileId") Integer fileId,
                                          @RequestParam("file") MultipartFile file,
                                          @RequestParam("extension") String extension);

    @Operation(summary = "Get all Chat Messages by ChatRoom")
    @GetMapping(path = "{chatId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<ChatMessageResponse> findAllMessages(ChatMessageSpecification specification,
                                                           @PathVariable("chatId") Integer chatId,
                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                           @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                           @RequestParam(name = "sort-attribute", defaultValue = "creationTime") String attribute);

    @Operation(summary = "Get all Chat Messages by ChatRooms")
    @GetMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<ChatMessageResponse> findAllChatMessages(ChatMessageSpecification specification,
                                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                               @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                               @RequestParam(name = "sort-attribute", defaultValue = "creationTime") String attribute,
                                                               @RequestParam(name = "value", defaultValue = "") String value,
                                                               @RequestParam(name = "advanceSearch", defaultValue = "NO") String advanceSearch);

    @Operation(summary = "Get unread messages")
    @GetMapping(path = "{chatId}/messages/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<ChatMessageResponse> findUnread(@PathVariable("chatId") Integer chatId,
                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "40") Integer size);

    @Operation(summary = "Get all archive to Chat Rooms")
    @GetMapping(path = "/archive", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<ChatRoomResponse> findAllChatArchive(final ChatRoomv2Specification specification,
                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                           @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                           @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute);


    @Operation(summary = "Read a Message in a Chat Room")
    @PostMapping(path = "{chatId}/messages/read")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void readMessage(@PathVariable("chatId") Integer chatId);


    @Operation(summary = "Archive a Message in a Chat Room")
    @PutMapping(path = "{chatId}/archive")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void archiveMessage(@PathVariable("chatId") Integer chatId);

    @Operation(summary = "Get last messages")
    @GetMapping(path = "{chatId}/messages/{messageId}/last", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<ChatMessageResponse> findLast(@PathVariable("chatId") Integer chatId,
                                       @PathVariable("messageId") Integer messageId,
                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", defaultValue = "20") Integer size);

    @Operation(summary = "Get next messages")
    @GetMapping(path = "{chatId}/messages/{messageId}/next", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<ChatMessageResponse> findNext(@PathVariable("chatId") Integer chatId,
                                       @PathVariable("messageId") Integer messageId,
                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", defaultValue = "20") Integer size);

    @Operation(summary = "Save a Out of Office")
    @PostMapping(path = "/outoffice", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ChatOutOfficeResponse saveOutOfOffice(@RequestBody @Valid OutOfficeRequest request);

    @Operation(summary = "Get all Chat og OutOffice")
    @GetMapping(path = "/outoffice", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<ChatOutOfficeResponse> findAllOutOfOffice(ChatOutOfficeSpecification specification,
                                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                                @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                                @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute
    );

    @Operation(summary = "Delete a Out of Office by its id hard delete ")
    @DeleteMapping(path = "/outoffice/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void deleteById(@PathVariable("id") Integer id);

    @Operation(summary = "Delete a Chat file of Message by its id hard delete ")
    @DeleteMapping(path = "/messages/{messageId}/files/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void deleteChatMessageFile(@PathVariable("messageId") Integer messageId,@PathVariable("id") Integer id);

    @Operation(summary = "Get a Out of Office by its id")
    @GetMapping(value = "/outoffice/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    ChatOutOfficeResponse findById(@PathVariable("id") Integer id);

    @Operation(summary = "Get Last Message")
    @GetMapping(value = "/messages/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    boolean findByLastMessage();
}
