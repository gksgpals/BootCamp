package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;

import java.sql.SQLException;
import java.util.Optional;
import  java.util.List;

public interface  MemberRepository {
    Member save(Member member) throws SQLException;
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

    default void clearStore() {

    }
}
