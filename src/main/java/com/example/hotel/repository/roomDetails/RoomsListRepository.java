package com.example.hotel.repository.roomDetails;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.roomDetails.RoomsList;

public interface RoomsListRepository extends JpaRepository<RoomsList, Long> {

	void deleteByRoomListIdAndDeletedTrue(Long id);

}
