package com.example.eventhunter.collaborator.service;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.List;

import androidx.lifecycle.Observer;

public interface CollaboratorService {
    void getAllCollaborators(Observer<List<CollaboratorHeader>> observer);
}
