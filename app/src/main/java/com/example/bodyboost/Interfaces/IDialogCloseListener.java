package com.example.bodyboost.Interfaces;

public interface IDialogCloseListener {
    enum Action {
        DELETE,
        ADD
    }

    public void handleDialogClose(Action action, Long doseId);
}
