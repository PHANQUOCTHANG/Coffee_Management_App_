package com.example.javafxapp.Controller.Admin.Member;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Model.Member;
import com.example.javafxapp.Service.MemberService;
import com.example.javafxapp.Validation.ValidationMember;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateMemberController {

    @FXML private TextField phoneField;
    @FXML private TextField pointField;
    @FXML private JFXButton btnUpdate;

    private MemberService memberService;
    private MemberController memberController;
    public static int member_id = -1;

    public void setMemberController(MemberController controller) {
        this.memberController = controller;
    }

    @FXML
    private void initialize() {
        memberService = new MemberService();
        Member member = memberService.findMemberByID(member_id);
        if (member != null) {
            phoneField.setText(member.getMemberPhone());
            pointField.setText(String.valueOf(member.getPoint()));
        }
    }

    @FXML
    private void updateMember() {
        String phone = phoneField.getText().trim();
        String pointText = pointField.getText().trim();

        if (!ValidationMember.validationPhone(phone) || !ValidationMember.validationPoint(pointText)) return;

        try {
            int point = Integer.parseInt(pointText);
            Member updatedMember = new Member(member_id, phone, point);
            memberService.updateMember(updatedMember);
            memberController.loadMembers();
            AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật hội viên thành công");

            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật hội viên");
        }
    }
}
