package lastie_wangechian_Final.com.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import lastie_wangechian_Final.com.R;

public class IntroActivity extends AppCompatActivity {

    Animation btnAnim;
    int position = 0;
    private ViewPager screenPager;
    private TabLayout tab_indicator;
    private Button btnNext;
    private Button btngetStarted;
    private IntroViewPagerAdapter introViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //checking if the animation slide is attended already
        if (restorePrefData()) {

            Intent next_intent = new Intent(getApplicationContext(), SelectActivity.class);
            startActivity(next_intent);
            finish();

        }

        //concetrating on making the activiy_intro xml the main deal here
        setContentView(R.layout.activity_intro);

        //variables from layouts
        tab_indicator = findViewById(R.id.tab_indicator);
        btnNext = findViewById(R.id.btn_Next);
        btngetStarted = findViewById(R.id.btn_getStarted);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        //fill list screen
        final ArrayList<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Easy Pay", "Easy to pay or Pay on Delivery.", R.drawable.easy_1));
        mList.add(new ScreenItem("Clean Water", "Sanitized water and from sources with licensed permits", R.drawable.water_2));
        mList.add(new ScreenItem("Ready for consumption", "The water is ready for human and animal consumption", R.drawable.water_3));
        mList.add(new ScreenItem("Affordable Delivery", "The system offers affordable delivery to your residence,", R.drawable.deliv_3));
        mList.add(new ScreenItem("We care", "we respect human life and care of our surrounding ", R.drawable.deliv_2));


        //setup viewpager
        screenPager = findViewById(R.id.screen_pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //set tablayouts with view pager
        tab_indicator.setupWithViewPager(screenPager);


        //next button clicks action
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();

                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);

                }

                //the last screen in the introduction slider
                if (position == mList.size() - 1) {

                    //kuna kazi hapa
                    loadLastScreen();
                }
            }
        });

        //tablayout add change listner
        tab_indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size() - 1) {

                    loadLastScreen();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btngetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next_intent = new Intent(getApplicationContext(), SelectActivity.class);
                startActivity(next_intent);

                savePrefsData();
                finish();

            }
        });
    }

    private boolean restorePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }


    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();


    }

    //hiding the tab indicator and the next button
    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btngetStarted.setVisibility(View.VISIBLE);
        tab_indicator.setVisibility(View.INVISIBLE);

        //setting up animation for button get started
        btngetStarted.setAnimation(btnAnim);
    }
}