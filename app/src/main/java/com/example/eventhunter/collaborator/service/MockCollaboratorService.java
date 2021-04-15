package com.example.eventhunter.collaborator.service;

import com.example.eventhunter.collaborator.ui.header.CollaboratorHeader;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

public class MockCollaboratorService implements CollaboratorService {
    @Override
    public void getAllCollaborators(Observer<List<CollaboratorHeader>> observer) {
        List<CollaboratorHeader> collaboratorHeaders = new ArrayList<>();

        collaboratorHeaders.add(new CollaboratorHeader("First"));
        collaboratorHeaders.add(new CollaboratorHeader("Second"));
        collaboratorHeaders.add(new CollaboratorHeader("Finish"));

        observer.onChanged(collaboratorHeaders);
    }
}
