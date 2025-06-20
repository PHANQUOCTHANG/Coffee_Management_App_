package com.example.javafxapp.Controller.Admin.Member;

import com.example.javafxapp.Helpper.AlertInfo;
import com.example.javafxapp.Helpper.Pages;
import com.example.javafxapp.Model.Member;
import com.example.javafxapp.Service.MemberService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.text.Collator;
import java.util.*;

public class MemberController implements Initializable {

    @FXML private TextField searchField;
    @FXML private JFXButton btnAdd;
    @FXML private JFXCheckBox checkBoxAll;

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, Boolean> checkBoxColumn;
    @FXML private TableColumn<Member, Integer> indexColumn;
    @FXML private TableColumn<Member, String> phoneColumn;
    @FXML private TableColumn<Member, String> pointColumn;
    @FXML private TableColumn<Member, HBox> actionColumn;

    @FXML private Label memberCountLabel, statusLabel;

    private final MemberService memberService = new MemberService();
    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
    private final ObservableList<Member> filteredList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchField.textProperty().addListener((obs, oldText, newText) -> applyFilters());
        setupTableColumns();
        loadMembers();
    }

    private void setupTableColumns() {
        // Checkbox column
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(col -> new TableCell<Member, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                CheckBox checkBox = new CheckBox();
                checkBox.setAlignment(Pos.CENTER);
                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                Member member = getTableRow().getItem();
                checkBox.selectedProperty().unbindBidirectional(member.selectedProperty());
                checkBox.selectedProperty().bindBidirectional(member.selectedProperty());

                setGraphic(checkBox);
            }
        });

        // Index column
        indexColumn.setCellValueFactory(cellData -> {
            int index = memberTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleObjectProperty<>(index);
        });
        indexColumn.setStyle("-fx-alignment: CENTER;");

        // ✅ Phone column
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMemberPhone()));
        phoneColumn.setStyle("-fx-alignment: CENTER;");

        // Point column
        pointColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPoint())));
        pointColumn.setStyle("-fx-alignment: CENTER;");

        // Action column
        actionColumn.setCellValueFactory(cellData -> {
            Member member = cellData.getValue();
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            JFXButton editButton = new JFXButton("Sửa");
            editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 10px;");
            editButton.setOnAction(e -> updateMember(member));

            JFXButton deleteButton = new JFXButton("Xóa");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10px;");
            deleteButton.setOnAction(e -> deleteMember(member));

            actionBox.getChildren().addAll(editButton, deleteButton);
            return new SimpleObjectProperty<>(actionBox);
        });
    }

    public void loadMembers() {
        try {
            memberList.clear();
            memberList.addAll(memberService.getAllMembers());
            filteredList.clear();
            filteredList.addAll(memberList);
            memberTable.setItems(filteredList);

            updateDisplayStatus();
        } catch (Exception e) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách thành viên");
        }
    }

    private void updateDisplayStatus() {
        int total = filteredList.size();
        memberCountLabel.setText("Tổng số thành viên: " + total);
        if (statusLabel != null) {
            statusLabel.setText(total > 0 ? "Đã tải " + total + " thành viên" : "Không có thành viên nào");
        }
    }

    @FXML
    private void checkBoxAll(ActionEvent event) {
        boolean selected = checkBoxAll.isSelected();
        for (Member member : memberList) {
            member.setSelected(selected);
        }
        memberTable.refresh();
    }

    @FXML
    private void addMember(ActionEvent event) {
        Pages.pageAddMember(this);
    }

    private void updateMember(Member member) {
        Pages.pageUpdateMember(member.getMemberId(), this);
    }

    private void deleteMember(Member member) {
        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
            try {
                memberService.deleteMember(member.getMemberId());
                loadMembers();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void deleteAll(ActionEvent event) {
        List<Member> selectedMembers = new ArrayList<>();
        for (Member member : filteredList) {
            if (member.isSelected()) {
                selectedMembers.add(member);
            }
        }

        if (selectedMembers.isEmpty()) {
            AlertInfo.showAlert(Alert.AlertType.ERROR, "Cảnh báo", "Vui lòng chọn ít nhất một thành viên để xóa");
            return;
        }

        if (AlertInfo.confirmAlert("Bạn có chắc muốn xóa không ?")) {
            try {
                for (Member member : selectedMembers) {
                    memberService.deleteMember(member.getMemberId());
                }
                loadMembers();
                AlertInfo.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành công");
            } catch (Exception e) {
                e.printStackTrace();
                AlertInfo.showAlert(Alert.AlertType.ERROR, "Thất bại", "Xóa thất bại");
            }
        }
    }

    @FXML
    private void searchMember() {
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim();

        filteredList.clear();
        memberTable.getItems().clear();
        memberTable.refresh();

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        List<Member> list = (searchText.isEmpty())
                ? memberList
                : memberService.findMembersByKeyword(searchText);

        filteredList.addAll(list);
        updateDisplayStatus();
    }
}
