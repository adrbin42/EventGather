package com.hackathon.meetup.repository;

import com.hackathon.meetup.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

/**
 * Created by David Turk on 8/9/17.
 */

@Repository
public interface EventRepo extends JpaRepository<Event, Integer> {
}
