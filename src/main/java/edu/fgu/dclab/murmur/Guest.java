package edu.fgu.dclab.murmur;

import edu.fgu.dclab.ChatMessage;
import edu.fgu.dclab.LoginMessage;
import edu.fgu.dclab.Message;
import edu.fgu.dclab.RoomMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.text.MessageFormat;

public class Guest implements MessageSink, MessageSource {
    private final int LOGINING = 0; //登入狀態, 登入前.
    private final int CHATTING = 1; //聊天狀態, 登入成功後.



    private String id = "";
    private SceneChat scene = null;
    private int state = LOGINING;

    private MessageSink sink = null;

    public Guest(SceneChat scene) {
        this.scene = scene;

        scene.setOnButtonSend(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                readMessage();
            }
        });
    }

    @Override
    public Message readMessage() {
        Message message = null;

        switch (this.state) {   //依不同狀態進行不同處理.
            case CHATTING:  //產生聊天訊息準備送出.
                message = new ChatMessage(
                    this.id, this.scene.readMessage()
                );

                break;

            case LOGINING:
                this.id = this.scene.readMessage();

                message = new LoginMessage(
                    this.id, ""
                );

                this.state = CHATTING;

                break;

            default:
        }

        this.sink.writeMessage(message);

        return message;
    }

    private void process(Message message) {
        switch (message.getType()) {
            case Message.CHAT:
                String chat = MessageFormat.format( //將取得的訊息轉為字串.
                    "{0}： {1}",
                    message.getSource(),    //取得誰說的.
                    ((ChatMessage) message).MESSAGE //取得訊息內容.
                );

                scene.writeMessage(chat);

                break;

            case Message.ROOM_STATE:    //如果是聊天室訊息.
                scene.updateStatusBar(
                    ((RoomMessage)message).NUMBER_OF_GUESTS,    //更新人數.
                    ((RoomMessage)message).ROOM_NUMBER  //更新聊天室編號
                );

                break;

            default:
        }
    }

    @Override
    public void connectSink(MessageSink sink) {
        this.sink = sink;
    }

    @Override
    public void writeMessage(Message message) {
        process(message);
    }
}
