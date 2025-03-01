package com.plweegie.android.squashtwo.data;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repos", indices = {@Index(value = "name", unique = true)})

public class RepoEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("id")
    @Expose
    private long repoId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("stargazers_count")
    @Expose
    private int stargazersCount;
    @SerializedName("watchers_count")
    @Expose
    private int watchersCount;
    @SerializedName("owner")
    @Expose
    @Embedded
    private Owner owner;

    @Ignore
    public RepoEntry() {}

    @Ignore
    public RepoEntry(long repoId, String name, String createdAt, String language,
                     int stargazersCount, int watchersCount, String ownerLogin) {
        this.repoId = repoId;
        this.name = name;
        this.createdAt = createdAt;
        this.language = language;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
        this.owner.login = ownerLogin;
    }

    public RepoEntry(int id, long repoId, String name, String createdAt, String language,
                     int stargazersCount, int watchersCount, Owner owner) {
        this.id = id;
        this.repoId = repoId;
        this.name = name;
        this.createdAt = createdAt;
        this.language = language;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
        this.owner = owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setRepoId(long id) {
        this.repoId = id;
    }

    public long getRepoId() {
        return repoId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public static class Owner {

        @SerializedName("login")
        @Expose
        private String login;

        @Ignore
        public Owner() {}

        public Owner(String login) {
            this.login = login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getLogin() {
            return login;
        }
    }
}
