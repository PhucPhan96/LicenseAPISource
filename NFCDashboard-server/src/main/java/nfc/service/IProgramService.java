package nfc.service;

import java.util.List;

import nfc.model.Program;

public interface IProgramService {
	List<Program> getListProgram();
	boolean insertProgram(Program prog);
}