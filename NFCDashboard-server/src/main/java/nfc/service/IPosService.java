package nfc.service;

import nfc.model.ViewModel.PosDetailView;

public interface IPosService {
	PosDetailView getPosDetailView(int orderId);
}
