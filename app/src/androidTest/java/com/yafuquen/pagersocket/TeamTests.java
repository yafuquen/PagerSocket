package com.yafuquen.pagersocket;

import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.text.TextUtils;
import android.view.View;

import com.yafuquen.pagersocket.data.service.RolesService;
import com.yafuquen.pagersocket.data.service.TeamService;
import com.yafuquen.pagersocket.data.service.TeamUpdateService;
import com.yafuquen.pagersocket.data.service.model.TeamMateResponse;
import com.yafuquen.pagersocket.di.ApplicationComponent;
import com.yafuquen.pagersocket.di.ApplicationModule;
import com.yafuquen.pagersocket.view.ui.TeamActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.yafuquen.pagersocket.RecyclerViewMatcher.withRecyclerView;
import static com.yafuquen.pagersocket.view.ui.util.DisplayUtils.getCountry;
import static com.yafuquen.pagersocket.view.ui.util.DisplayUtils.getLanguages;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TeamTests {

    private static final int teamSize = 4;

    @Rule
    public DaggerMockRule<ApplicationComponent> daggerRule = new DaggerMockRule<>(ApplicationComponent.class,
            new ApplicationModule(InstrumentationRegistry.getTargetContext()))
            .set(component -> {
                Application app = (Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
                app.setComponent(component);
            });

    @Mock
    private TeamService teamService;

    @Mock
    private RolesService rolesService;

    @Mock
    private TeamUpdateService teamUpdateService;

    @Rule
    public ActivityTestRule<TeamActivity> activityRule = new ActivityTestRule<>(TeamActivity.class, false, false);

    private final Map<String, String> roles = createRoles();

    private final List<TeamMateResponse> teamMateResponseList = createList();

    @Before
    public void setup() {
        when(rolesService.roles()).thenReturn(Observable.just(roles));
        when(teamService.team()).thenReturn(Observable.just(teamMateResponseList));
        when(teamUpdateService.receiveEvents()).thenReturn(Observable.empty());
        when(teamUpdateService.updateStatus(anyString())).thenReturn(Observable.empty());
        EmojiCompat.Config config = new BundledEmojiCompatConfig(InstrumentationRegistry.getTargetContext());
        EmojiCompat.init(config);
        activityRule.launchActivity(null);
    }

    @Test
    public void showTeam() {
        onView(withId(R.id.team_list)).check(matches(isDisplayed()));
        for (int position = 0; position < teamMateResponseList.size(); position++) {
            onView(withId(R.id.team_list)).perform(RecyclerViewActions.scrollToPosition(position));
            assertTextInRecyclerViewPosition(position, R.id.user_name, teamMateResponseList.get(position).getName());
            assertTextInRecyclerViewPosition(position, R.id.user_role, roles.get(Integer.toString(teamMateResponseList.get(position).getRole())));
            assertTextInRecyclerViewPosition(position, R.id.user_github, teamMateResponseList.get(position).getGithub());
            assertTextInRecyclerViewPosition(position, R.id.user_location, getCountry(teamMateResponseList.get(position).getLocation()));
            assertTextInRecyclerViewPosition(position, R.id.user_languages, getLanguages(teamMateResponseList.get(position).getLanguages()));
            assertTextInRecyclerViewPosition(position, R.id.user_tags, TextUtils.join(", ", teamMateResponseList.get(position).getTags()));
        }
    }

    @Test
    public void showDetail() {
        onView(withId(R.id.team_list)).check(matches(isDisplayed()));
        for (int position = 0; position < teamMateResponseList.size(); position++) {
            onView(withId(R.id.team_list)).perform(RecyclerViewActions.scrollToPosition(position));
            onView(withRecyclerView(R.id.team_list).atPosition(position)).perform(click());
            assertText(withId(R.id.user_name), teamMateResponseList.get(position).getName());
            assertText(withId(R.id.user_role), roles.get(Integer.toString(teamMateResponseList.get(position).getRole())));
            assertText(withId(R.id.user_github), teamMateResponseList.get(position).getGithub());
            assertText(withId(R.id.user_location), getCountry(teamMateResponseList.get(position).getLocation()));
            assertText(withId(R.id.user_languages), getLanguages(teamMateResponseList.get(position).getLanguages()));
            assertText(withId(R.id.user_tags), TextUtils.join(", ", teamMateResponseList.get(position).getTags()));
            pressBack();
        }
    }

    @Test
    public void updateStatus() {
        onView(withId(R.id.team_list)).check(matches(isDisplayed()));
        String state;
        for (int position = 0; position < teamMateResponseList.size(); position++) {
            state = "status" + (position + 1);
            onView(withId(R.id.team_list)).perform(RecyclerViewActions.scrollToPosition(position));
            onView(withRecyclerView(R.id.team_list).atPositionOnView(position, R.id.user_card)).perform(click());
            onView(withId(R.id.user_status_edit_action)).perform(scrollTo(), click());
            onView(withId(R.id.user_status_edit)).check(matches(isDisplayed())).perform(scrollTo(), typeText(state));
            onView(withId(R.id.user_status_apply_action)).perform(click());
            onView(withId(R.id.team_list)).perform(RecyclerViewActions.scrollToPosition(position));
            assertTextInRecyclerViewPosition(position, R.id.user_status, state);
        }
    }

    private void assertTextInRecyclerViewPosition(int position, @IdRes int viewId, String text) {
        assertText(withRecyclerView(R.id.team_list).atPositionOnView(position, viewId), text);
    }

    private void assertText(Matcher<View> viewMatcher, String text) {
        onView(viewMatcher).check(matches(withText(text)));
    }

    private List<TeamMateResponse> createList() {
        List<TeamMateResponse> teamMateResponseList = new ArrayList<>();
        for (int i = 1; i <= teamSize; i++) {
            teamMateResponseList.add(new TeamMateResponse("Name" + i, "https://avatars0.githubusercontent.com/u/9919?s=280&v=4",
                    "github" + i, 1, "Male", Collections.singletonList("en"), Collections.singletonList("Skills" + i), "us"));
        }
        return teamMateResponseList;
    }

    private Map<String, String> createRoles() {
        Map<String, String> roles = new HashMap<>();
        roles.put("1", "role1");
        return roles;
    }
}
