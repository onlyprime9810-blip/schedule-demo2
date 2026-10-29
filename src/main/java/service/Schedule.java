package service;

import exception.ScheduleConflictException;
import exception.ValidationException;
import model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Schedule {
    private final LocalDate semesterStart;
    private final Set<LocalDate> holidays = new HashSet<>();
    private final List<ScheduleEntry> entries = new ArrayList<>();

    public Schedule(LocalDate semesterStart) {
        this.semesterStart = Objects.requireNonNull(semesterStart, "semesterStart");
    }

    public void addHoliday(LocalDate date) {
        holidays.add(Objects.requireNonNull(date));
    }

    public void add(WeekDay day, Lesson lesson, Audience audience) {
        int need = switch (lesson) {
            case Lecture l -> l.groups().stream().mapToInt(Group::size).sum();
            case Practice p -> p.groups().get(0).size();
            case Lab lab -> lab.groups().get(0).size() / 2 + 1;
        };

        if (audience.capacity() < need) {
            throw new ValidationException("Аудитория " + audience + " мала: нужно " + need);
        }

        for (var e : entries) {
            if (e.day() != day) continue;
            if (e.lesson().pair() != lesson.pair()) continue;
            if (e.audience().equals(audience)) {
                throw new ScheduleConflictException("Аудитория занята: " + e, e);
            }
            if (e.lesson().teacher().equals(lesson.teacher())) {
                throw new ScheduleConflictException("Преподаватель занят: " + e, e);
            }
            var busy = e.lesson().groups().stream().map(Group::code).collect(Collectors.toSet());
            var overlap = lesson.groups().stream().map(Group::code).filter(busy::contains).toList();
            if (!overlap.isEmpty()) {
                throw new ScheduleConflictException("Группа(ы) заняты " + overlap + ": " + e, e);
            }
        }
        entries.add(new ScheduleEntry(day, lesson, audience));
    }

    public List<ScheduleEntry> byGroup(String groupCode) {
        return entries.stream()
                .filter(e -> e.lesson().groups().stream().anyMatch(g -> g.code().equals(groupCode)))
                .sorted(Comparator.comparing(ScheduleEntry::day).thenComparing(e -> e.lesson().pair()))
                .toList();
    }

    public List<ScheduleEntry> byTeacher(String teacherId) {
        return entries.stream()
                .filter(e -> e.lesson().teacher().id().equals(teacherId))
                .sorted(Comparator.comparing(ScheduleEntry::day).thenComparing(e -> e.lesson().pair()))
                .toList();
    }

    public List<ScheduleEntry> byAudience(Audience audience) {
        return entries.stream()
                .filter(e -> e.audience().equals(audience))
                .sorted(Comparator.comparing(ScheduleEntry::day).thenComparing(e -> e.lesson().pair()))
                .toList();
    }

    public int teacherLoadHours(String teacherId) {
        long pairs = entries.stream().filter(e -> e.lesson().teacher().id().equals(teacherId)).count();
        return (int) (pairs * 2);
    }

    public List<Map.Entry<LocalDate, ScheduleEntry>> range(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) throw new ValidationException("to < from");
        List<Map.Entry<LocalDate, ScheduleEntry>> result = new ArrayList<>();
        for (var d = from; !d.isAfter(to); d = d.plusDays(1)) {
            if (holidays.contains(d)) continue;
            var day = toWeekDay(d);
            if (day == null) continue;
            int weekNumber = (int) (java.time.temporal.ChronoUnit.DAYS.between(semesterStart, d) / 7) + 1;
            for (var e : entries) {
                if (e.day() != day) continue;
                if (WeekParity.EVERY.matches(weekNumber)) {
                    result.add(Map.entry(d, e));
                }
            }
        }
        result.sort(Map.Entry.comparingByKey());
        return result;
    }

    public LocalDate[] findFreeWindow(Audience audience, LocalDate from, Duration duration, int horizonDays) {
        if (duration.isNegative() || duration.isZero()) throw new ValidationException("duration <= 0");
        var busy = byAudience(audience);
        for (int i = 0; i < horizonDays; i++) {
            var date = from.plusDays(i);
            if (holidays.contains(date)) continue;
            var wd = toWeekDay(date);
            if (wd == null) continue;
            var dayBusy = busy.stream().filter(e -> e.day() == wd)
                    .map(e -> e.lesson().pair())
                    .collect(Collectors.toSet());
            LocalTime t = LocalTime.of(8, 0);
            while (!t.plus(duration).isAfter(LocalTime.of(20, 0))) {
                boolean free = true;
                for (var p : dayBusy) {
                    if (t.isBefore(p.end()) && t.plus(duration).isAfter(p.start())) {
                        free = false;
                        break;
                    }
                }
                if (free) return new LocalDate[]{date, null};
                t = t.plusMinutes(10);
            }
        }
        throw new ValidationException("Свободное окно не найдено за " + horizonDays + " дн.");
    }

    private static WeekDay toWeekDay(LocalDate d) {
        return switch (d.getDayOfWeek()) {
            case MONDAY -> WeekDay.MONDAY;
            case TUESDAY -> WeekDay.TUESDAY;
            case WEDNESDAY -> WeekDay.WEDNESDAY;
            case THURSDAY -> WeekDay.THURSDAY;
            case FRIDAY -> WeekDay.FRIDAY;
            case SATURDAY -> WeekDay.SATURDAY;
            case SUNDAY -> null;
        };
    }
}