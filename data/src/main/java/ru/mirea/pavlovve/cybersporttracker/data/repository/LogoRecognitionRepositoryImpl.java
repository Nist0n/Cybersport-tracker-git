package ru.mirea.pavlovve.cybersporttracker.data.repository;

import ru.mirea.pavlovve.cybersporttracker.data.source.TfliteLogoDataSource;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.LogoRecognitionRepository;

public class LogoRecognitionRepositoryImpl implements LogoRecognitionRepository {

    private final TfliteLogoDataSource logoDataSource;

    public LogoRecognitionRepositoryImpl(TfliteLogoDataSource logoDataSource) {
        this.logoDataSource = logoDataSource;
    }

    @Override
    public String recognizeLogo(String imageUri) {
        return logoDataSource.classify(imageUri);
    }
}
