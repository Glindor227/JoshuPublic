package com.example.joshu.mvp.model.entity

enum class DeleteStatusEnum(val value: Int) {
    No(0),
    Deleting(1),
    Deleted(2);

    companion object {
        val DEFAULT = No
    }
}