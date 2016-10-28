package me.droan.netsu.family;

interface FamilyContract {
    interface View {
        void start();

        void updateAdapter(FamilyAdapter adapter);

        void openTracker(String trackerId);

        void showAddChild();
    }

    interface UserActionListener {

        void createAdapter();
    }
}
