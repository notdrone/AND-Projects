package me.droan.netsu.common;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import me.droan.netsu.model.Child;
import me.droan.netsu.model.LastMedicine;
import me.droan.netsu.model.LastTemperature;
import me.droan.netsu.model.Reading;

/**
 * Created by Drone on 16/09/16.
 */

public class MappingHelper {

    public static HashMap<String, Object> addNewChild(Child child, DatabaseReference reference) {
        HashMap<String, Object> rootMap = new HashMap<>();
        HashMap<String, Object> childMap = Utils.convertToMap(child);
        HashMap<String, Object> childMapForTracker = Utils.convertToMap(child);

        String eid = Utils.getEid();
        String childId = Utils.generatePushId(reference);
        rootMap.put(Constants.PATH_FAMILIES + "/" + eid + "/" + childId, childMap);
        childMapForTracker.put("childId", childId);
        rootMap.put(Constants.PATH_TRACKER + "/" + child.getTrackerId() + "/" + Constants.PATH_TRACKER_ABOUT, childMapForTracker);
        return rootMap;
    }

    public static HashMap<String, Object> addNewTemperature(String trackerId, String childId, Reading reading, DatabaseReference reference) {
        HashMap<String, Object> rootMap = new HashMap<>();
        HashMap<String, Object> readingMap = Utils.convertToMap(reading);
        rootMap.put(Constants.PATH_TRACKER + "/" + trackerId + "/" + Constants.PATH_TRACKER_DATA + "/" + Utils.generatePushId(reference), readingMap);
        LastTemperature temperature = new LastTemperature(reading.getTemperature(), reading.getReverseTimeStamp(), reading.getReverseTimeStamp());
        HashMap<String, Object> temperatureMap = Utils.convertToMap(temperature);
        rootMap.put(Constants.PATH_FAMILIES + "/" + Utils.getEid() + "/" + childId + "/" + Constants.PATH_LAST_TEMPERATURE, temperatureMap);
        return rootMap;
    }

    public static HashMap<String, Object> addNewMedicine(String trackerId, String childId, Reading reading, DatabaseReference ref) {
        HashMap<String, Object> rootMap = new HashMap<>();
        HashMap<String, Object> readingMap = Utils.convertToMap(reading);
        rootMap.put(Constants.PATH_TRACKER + "/" + trackerId + "/" + Constants.PATH_TRACKER_DATA + "/" + Utils.generatePushId(ref), readingMap);
        rootMap.put(Constants.PATH_MEDICINES + "/" + Utils.getEid() + "/" + Utils.getMedicineKey(reading.getMedicine()), reading.getMedicine());
        LastMedicine lastMedicine = new LastMedicine(reading.getMedicine(), reading.getReverseTimeStamp(), reading.getReverseTimeStamp());
        HashMap<String, Object> lastMedicineMap = Utils.convertToMap(lastMedicine);
        rootMap.put(Constants.PATH_FAMILIES + "/" + Utils.getEid() + "/" + childId + "/" + Constants.PATH_LAST_MEDICINE, lastMedicineMap);
        return rootMap;
    }
}
