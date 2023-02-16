package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.dto.out.ResponseMeetingDatesDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MeetingDao {
    @Select("select dt.date_time as dateTime, date_format(dt.date_time, '%Y-%m-%d') as date, date_format(dt.date_time, '%H:%i:%s') as time, m.personnel, m.max_time as maxTime, m.price,\n" +
            "       m.date_policy as datePolicy, count(r.reservation_id) as currentStaff, m.category,\n" +
            "       if ( count(r.reservation_id) >= m.personnel, 'false', 'true') as availability\n" +
            "    from date_time as dt\n" +
            "    inner join meeting m on dt.meeting_id = m.meeting_id\n" +
            "    left join reservation r on m.meeting_id = r.meeting_id\n" +
            "                                   and dt.date_time >= r.start_date_time\n" +
            "                                   and dt.date_time < r.end_date_time\n" +
            "                                   and (r.reservation_state = 'PAYMENT_SUCCESS' or r.reservation_state = 'PAYMENT_PROGRESS' or r.reservation_state = 'ACCEPT')\n" +
            "    where dt.meeting_id = #{meetingId} and month(dt.date_time) = #{month}\n" +
            "    group by dateTime, date, time, m.personnel, m.max_time, m.price, m.category, m.date_policy")
    List<ResponseMeetingDatesDto> getMonthlyReservationSchedule(Long meetingId, Integer month);

    @Select("SELECT meeting_id\n" +
            "FROM meeting\n" +
            "WHERE (CONCAT(end_date, ' ', end_time) < NOW())" +
            " AND meeting.meeting_state = 'OPEN';")
    List<Long> findExpiredMeetings();

    @Update({
            "<script>",
            "UPDATE meeting",
            "SET meeting_state = 'CLOSE'",
            "WHERE meeting_id IN",
            "<foreach collection='meetingIds' item='meetingId' index='index' open='(' separator=',' close=')'>",
            "#{meetingId}",
            "</foreach>",
            "</script>"
    })
    void updateMeetingStateToClose(@Param("meetingIds") List<Long> meetingIds);
}
