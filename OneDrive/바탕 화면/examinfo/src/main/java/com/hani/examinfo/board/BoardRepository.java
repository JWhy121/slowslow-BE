package com.hani.examinfo.board;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hani.examinfo.board.Board;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface BoardRepository extends JpaRepository<Board, Long> {
}
