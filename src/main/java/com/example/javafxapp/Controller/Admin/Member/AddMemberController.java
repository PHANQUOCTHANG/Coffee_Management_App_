package com.example.javafxapp.Controller.Admin.Member;

import com.example.javafxapp.Controller.Admin.Member.MemberController;
import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Member;
import com.example.javafxapp.Service.MemberService;
import com.example.javafxapp.Validation.ValidationMember;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMemberController {

    @FXML private TextField phoneField;
    @FXML private TextField pointField;
    @FXML private JFXButton btnAdd;

    private MemberService memberService;
    private MemberController memberController;

    public void setMemberController(MemberController controller) {
        this.memberController = controller;
    }

    @FXML
    private void initialize() {
        memberService = new MemberService();
    }

    @FXML
    private void addMember() {
        String phone = phoneField.getText().trim();
//        String pointText = pointField.getText().trim();

        if (!ValidationMember.validationPhone(phone)) return;


        try {
            Member isExistMmember = memberService.findMemberByPhone(phone);
            if (isExistMmember != null) {
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Hội viên đã tồn tại");
                return  ;
            }
            Member member = new Member(phone, 0);
            memberService.addMember(member);
            memberController.loadMembers();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm hội viên thành công");

            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm hội viên");
        }
    }
}
