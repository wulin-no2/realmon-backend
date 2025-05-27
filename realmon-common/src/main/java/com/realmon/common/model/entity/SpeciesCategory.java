package com.realmon.common.model.entity;


public enum SpeciesCategory {
    MAMMAL,
    BIRD,
    REPTILE,
    AMPHIBIAN,
    FISH,
    INSECT,
    ARACHNID,
    CRUSTACEAN,
    MOLLUSC,
    PLANT,
    FUNGI,
    ALGAE,
    MARINE_INVERTEBRATE,
    OTHER;

    /**
     * default icon for specific species
     * @return icon
     */
    public String getDefaultIcon() {
        return switch (this) {
            case BIRD -> "🐦";
            case MAMMAL -> "🦊";
            case INSECT -> "🐞";
            case PLANT -> "🌿";
            case FUNGI -> "🍄";
            case FISH -> "🐟";
            case MARINE_INVERTEBRATE -> "🪼";
            case REPTILE -> "🦎";
            case AMPHIBIAN -> "🐸";
            case ARACHNID -> "🕷";
            case MOLLUSC -> "🐚";
            case CRUSTACEAN -> "🦀";
            case ALGAE -> "🪸";
            default -> "❓";
        };
    }
}