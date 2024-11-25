package si.um.feri.vaja.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import si.um.feri.vaja.dao.TaskRepository;
import si.um.feri.vaja.vao.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class VajaController {

    @Autowired
    private TaskRepository taskRepository;

    // Simple hello endpoint to check if backend is running
    @GetMapping("/hello")
    public String hello() {
        return "Backend";
    }

    // GET all tasks
    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET a task by ID
    @GetMapping("/tasks/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id);
    }

    // POST (create) a new task
    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // PUT (update) a task by ID
    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setEndDate(updatedTask.getEndDate());
                    task.setStatus(updatedTask.getStatus());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    // DELETE a task by ID
    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

    // GET all tasks with optional filtering
    @GetMapping("/tasks/filter")
    public List<Task> getFilteredTasks(
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer priority) {

        // Preverimo, če je prejet datum v formatu yyyy-MM-dd
        System.out.println("Received endDate: " + endDate);
        System.out.println("Received priority: " + priority);

        if (endDate != null && priority != null) {
            return taskRepository.findByEndDateAndPriority(endDate, priority);
        } else if (endDate != null) {
            return taskRepository.findByEndDate(endDate);
        } else if (priority != null) {
            return taskRepository.findByPriority(priority);
        } else {
            return taskRepository.findAll();
        }
    }

}