package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Member;
import com.example.javafxapp.Repository.MemberRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private MemberRepository memberRepository = new MemberRepository();
    // Thêm thành viên mới
    public void addMember(Member member) {
        memberRepository.add(member);
    }

    // Cập nhật thông tin thành viên
    public void updateMember(Member member) {
        memberRepository.update(member);
    }

    // Xoá mềm (đặt deleted = true)
    public void deleteMember(int memberId) {
        memberRepository.delete(memberId);
    }

    // Lấy tất cả thành viên chưa bị xoá
    public List<Member> getAllMembers() {
        return memberRepository.getAll();
    }

    // Tìm thành viên theo ID
    public Member findMemberByID(int memberId) {
        return memberRepository.findByID(memberId);
    }

    // Tìm thành viên theo số điện thoại
    public Member findMemberByPhone(String phone) {
        return memberRepository.findByName(phone);
    }

    // Tìm thành viên theo từ khoá (số điện thoại)
    public List<Member> findMembersByKeyword(String keyword) {
        return memberRepository.findAllByKeyword(keyword);
    }
}
