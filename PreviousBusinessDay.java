import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * 前営業日取得クラス
 */
public class PreviousBusinessDay {
    
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate prevWorkday = getPreviousWorkday(today);
        System.out.println("前営業日: " + prevWorkday);
    }

    /**
     * 前営業日取得
     * @param date 日付
     * @return 前営業日
     */
    private static LocalDate getPreviousWorkday(LocalDate date) {

        // 前営業日
        LocalDate prevWorkDay = date.minusDays(1);
        
        // 祝日
        Set<LocalDate> holidays = getJapaneseHolidays(date);
        
        // 前営業日が土日・祝日の場合、調整
        while (isWeekend(prevWorkDay) || holidays.contains(prevWorkDay)) {
            prevWorkDay = prevWorkDay.minusDays(1);
        }
        
        return prevWorkDay;
    }

    /**
     * 土日取得
     * @param date 日付
     * @return Boolean
     */
    private static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 祝日取得
     * @param date 日付
     * @return 祝日
     */
    private static Set<LocalDate> getJapaneseHolidays(LocalDate date) {
        
        // 祝日リスト
        Set<LocalDate> holidays = new HashSet<>();

        // 祝日追加
        holidays.add(LocalDate.of(date.getYear(), 1, 1));   // 元日

        // 成人の日 １月の第２月曜日
        LocalDate comingOfAgeDay = LocalDate.of(date.getYear(), 1, 1); 
        while (comingOfAgeDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            comingOfAgeDay = comingOfAgeDay.plusDays(1);
        }
        comingOfAgeDay = comingOfAgeDay.plusDays(7);
        holidays.add(comingOfAgeDay);                       // 成人の日

        holidays.add(LocalDate.of(date.getYear(), 2, 11));  // 建国記念の日
        holidays.add(LocalDate.of(date.getYear(), 2, 23));  // 天皇誕生日

        // 春分の日を計算 (国立天文台, National Astronomical Observatory of Japan, NAOJ)
        int daysSince = date.getYear() - 1980;
        double equinox = 0.242194;
        int springDay = (int) Math.floor(20.8431 + equinox * daysSince - daysSince / 4);
        holidays.add(LocalDate.of(date.getYear(), 3, springDay));  // 春分の日

        holidays.add(LocalDate.of(date.getYear(), 4, 29));  // 昭和の日
        holidays.add(LocalDate.of(date.getYear(), 5, 3));   // 憲法記念日
        holidays.add(LocalDate.of(date.getYear(), 5, 4));   // みどりの日
        holidays.add(LocalDate.of(date.getYear(), 5, 5));   // こどもの日

        // 海の日 7月の第3月曜日
        LocalDate marineDay = LocalDate.of(date.getYear(), 7, 1); 
        while (marineDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            marineDay = marineDay.plusDays(1);
        }
        marineDay = marineDay.plusDays(14);
        holidays.add(marineDay);                             // 海の日

        holidays.add(LocalDate.of(date.getYear(), 8, 11));   // 山の日

        // 敬老の日 9月の第3月曜日
        LocalDate respectDay = LocalDate.of(date.getYear(), 9, 1); 
        while (respectDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            respectDay = respectDay.plusDays(1);
        }
        respectDay = respectDay.plusDays(14);
        holidays.add(respectDay);                            // 敬老の日
        
        // 秋分の日を計算 (国立天文台, National Astronomical Observatory of Japan, NAOJ)
        int autumnDay = (int) Math.floor(23.2488 + equinox * daysSince - daysSince / 4);
        // 秋分の日が水曜日の場合、火曜日を「国民の休日」として追加する。
        if (LocalDate.of(date.getYear(), 9, autumnDay).getDayOfWeek() == DayOfWeek.WEDNESDAY) {
            holidays.add(LocalDate.of(date.getYear(), 9, autumnDay - 1));   // 国民の休日
        }

        holidays.add(LocalDate.of(date.getYear(), 9, autumnDay));   // 秋分の日

        // スポーツの日 10月の第2月曜日
        LocalDate sportDay = LocalDate.of(date.getYear(), 10, 1); 
        while (sportDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            sportDay = sportDay.plusDays(1);
        }
        sportDay = sportDay.plusDays(7);
        holidays.add(sportDay);                              // スポーツの日

        holidays.add(LocalDate.of(date.getYear(), 11, 3));   // 文化の日
        holidays.add(LocalDate.of(date.getYear(), 11, 23));  // 勤労感謝の日

        // 祝日調整追加
        holidays = adjustedHolidays(holidays);

        return holidays;
    }

    /**
     * 祝日調整追加
     * @param holidays 祝日リスト
     * @return 祝日リスト
     */
    private static Set<LocalDate> adjustedHolidays(Set<LocalDate> holidays) {
        
        // 祝日調整セット
        Set<LocalDate> adjustedHolidays = new HashSet<>(holidays);
        // 祝日移動
        for (LocalDate holiday : holidays) {
            if (holiday.getDayOfWeek() == DayOfWeek.SUNDAY) {
                // 調整日
                LocalDate moveDay = holiday;
                // 祝日が日曜日の場合、月曜日に移動
                while (moveDay.getDayOfWeek() != DayOfWeek.MONDAY) {
                    moveDay = moveDay.plusDays(1);
                }
                // 祝日が祝日リストに存在する場合、翌日に移動
                while (holidays.contains(moveDay)) {
                    moveDay = moveDay.plusDays(1);
                }
                adjustedHolidays.add(moveDay);
            }
        }

        return adjustedHolidays;
    }
}
