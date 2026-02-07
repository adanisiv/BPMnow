package com.example.bpmnow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.dark(Color.TRANSPARENT)
        );
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get NavController from NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        // Set initial graph
        navController.setGraph(R.navigation.nav_graph);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and navigate accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If user is not signed in, navigate to signIn fragment
        if (currentUser != null) {
            navController.navigate(R.id.roleSelection);
        } else {
            navController.navigate(R.id.signIn);
        }
    }

    // Method to switch navigation graphs
    public void switchToGraphClubber() {
        navController.setGraph(R.navigation.nav_graph_clubber);
    }

    public void switchToGraphDJ() {
        navController.setGraph(R.navigation.nav_graph_dj);
    }


}