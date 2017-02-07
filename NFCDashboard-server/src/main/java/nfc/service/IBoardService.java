package nfc.service;

import java.util.List;

import nfc.model.Board;
import nfc.model.Thread;

public interface IBoardService {
	boolean insertBoard(Board board);
	List<Board> getListBoard(String userName);
	List<Thread> getListThread(int board_id);
}
