package service.tradeservice.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class ChatMessageForm {
    // 메시지  타입 : 입장, 채팅
    // 메시지 타입에 따라서 동작하는 구조가 달라진다.
    // 입장과 퇴장 ENTER 과 LEAVE 의 경우 입장/퇴장 이벤트 처리가 실행되고,
    // TALK 는 말 그대로 내용이 해당 채팅방을 SUB 하고 있는 모든 클라이언트에게 전달된다.
    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    private MessageType type; // 메시지 타입
    private Long chatRoomId; // 방 번호
    private Long sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; //시간
}
