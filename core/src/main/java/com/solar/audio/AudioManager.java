package com.solar.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class AudioManager {
    private ObjectMap<String, Music> musicCache;
    private ObjectMap<String, Sound> soundCache;
    private Music currentMusic;
    private float musicVolume = 0.5f;
    private float soundVolume = 0.7f;
    private boolean musicEnabled = true;
    private boolean soundEnabled = true;

    public AudioManager() {
        musicCache = new ObjectMap<>();
        soundCache = new ObjectMap<>();
    }

    // ==================== MUSIC ====================

    public void playMusic(String path) {
        playMusic(path, true);
    }

    public void playMusic(String path, boolean loop) {
        if (!musicEnabled) return;

        // Stop current music
        if (currentMusic != null) {
            currentMusic.stop();
        }

        // Load or get from cache
        if (!musicCache.containsKey(path)) {
            try {
                Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
                musicCache.put(path, music);
            } catch (Exception e) {
                Gdx.app.error("AudioManager", "Failed to load music: " + path, e);
                return;
            }
        }

        currentMusic = musicCache.get(path);
        currentMusic.setLooping(loop);
        currentMusic.setVolume(musicVolume);
        currentMusic.play();

        Gdx.app.log("AudioManager", "Playing music: " + path);
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void pauseMusic() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    public void resumeMusic() {
        if (currentMusic != null && musicEnabled) {
            currentMusic.play();
        }
    }

    // ==================== SOUND EFFECTS ====================

    public void playSound(String path) {
        if (!soundEnabled) return;

        if (!soundCache.containsKey(path)) {
            try {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
                soundCache.put(path, sound);
            } catch (Exception e) {
                Gdx.app.error("AudioManager", "Failed to load sound: " + path, e);
                return;
            }
        }

        soundCache.get(path).play(soundVolume);
    }

    // ==================== VOLUME CONTROL ====================

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0f, Math.min(1f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = Math.max(0f, Math.min(1f, volume));
    }

    public float getMusicVolume() { return musicVolume; }
    public float getSoundVolume() { return soundVolume; }

    // ==================== ENABLE/DISABLE ====================

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled && currentMusic != null) {
            currentMusic.pause();
        } else if (enabled && currentMusic != null) {
            currentMusic.play();
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public boolean isMusicEnabled() { return musicEnabled; }
    public boolean isSoundEnabled() { return soundEnabled; }

    // ==================== CLEANUP ====================

    public void dispose() {
        // Dispose all music
        for (Music music : musicCache.values()) {
            music.dispose();
        }
        musicCache.clear();

        // Dispose all sounds
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();

        Gdx.app.log("AudioManager", "Audio resources disposed");
    }
}
