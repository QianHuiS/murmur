package edu.fgu.dclab.murmur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.text.MessageFormat;

public class SceneChat {
    private Scene mScene = null;

    private Label labelRoom = null;
    private Label labelGuest = null;
    private TextArea textArea = null;
    private TextField textField = null;

    private Button btnSend = null;

    public SceneChat() {
        BorderPane contentPane = new BorderPane();

        contentPane.setTop(getStatusBar());
        contentPane.setCenter(getMessagePane());
        contentPane.setBottom(getInputPane());

        mScene = new Scene(contentPane, 400, 200);
    } // SceneChat()

    public Scene getScene() {
        return mScene;
    }

    public String readMessage() {
        String message = this.textField.getText();
        this.textField.clear();

        return message;
    }
    //寫入訊息
    public void writeMessage(String message) {
        this.textArea.appendText(message + "\n");
    }
    //送出按鈕
    public void setOnButtonSend(EventHandler<ActionEvent> handler) {
        btnSend.setOnAction(handler);
    }
    //更新狀態列
    public void updateStatusBar(int numberOfGuests, int roomNumber) {
        Platform.runLater(new Runnable() {  //告訴FX想更新使用者介面的...部分
            @Override
            public void run() {
                labelRoom.setText(MessageFormat.format(
                    "房間號碼：{0}",
                    roomNumber
                ));
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelGuest.setText(MessageFormat.format(
                    "目前人數：{0}",
                    numberOfGuests
                ));
            }
        });
    }

    private ScrollPane getMessagePane() {
        ScrollPane scrollPane = new ScrollPane();

        this.textArea = new TextArea();
        this.textArea.setEditable(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.setContent(this.textArea);

        return scrollPane;
    } // getMessagePane()

    private BorderPane getInputPane() {
        btnSend = new Button("送 出");

        this.textField = new TextField();

        BorderPane inputPane = new BorderPane();

        inputPane.setCenter(this.textField);
        inputPane.setRight(btnSend);

        return inputPane;
    } // getInputPane()

    private HBox getStatusBar() {
        labelRoom = new Label("房間號碼： 0");
        labelGuest = new Label("目前人數： 0");

        return new HBox(labelRoom, labelGuest);
    } // getStatusBar()
}

// SceneChat.java