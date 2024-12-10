package repositroy.inter;

import model.EmploymentHistory;

import java.util.List;

public interface EmploymentHistoryRepository {
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int userId);
}
