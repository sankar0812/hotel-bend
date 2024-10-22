package com.example.hotel.service.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hotel.entity.detail.State;
import com.example.hotel.repository.detail.StateRepository;

@Service
public class StateService {

	@Autowired
	private StateRepository stateRepository;
	
	// view
		public List<State> listAll() {
			return this.stateRepository.findAll();
		}

		// save
		public State SaveStateDetails(State state) {
			return stateRepository.save(state);
		}

		// edit
		public State findById(Long stateId) {
			return stateRepository.findById(stateId).get();
		}

		// delete
		public void deleteStateId(Long id) {
			stateRepository.deleteById(id);
		}
	
}
