package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.repository.LogoRecognitionRepository;

public class RecognizeLogoUseCase {
    private final LogoRecognitionRepository logoRecognitionRepository;

    public RecognizeLogoUseCase(LogoRecognitionRepository logoRecognitionRepository) {
        this.logoRecognitionRepository = logoRecognitionRepository;
    }
    public String execute(String imageUri) {
        if (imageUri == null || imageUri.trim().isEmpty()) {
            return null;
        }
        return logoRecognitionRepository.recognizeLogo(imageUri.trim());
    }
}
