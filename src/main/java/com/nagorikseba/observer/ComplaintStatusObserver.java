package com.nagorikseba.observer;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.StatusUpdate;

public interface ComplaintStatusObserver {
    void onStatusChange(Complaint complaint, StatusUpdate update);
}
