package Project.AIROP.dto;

public record OutboxMetricsResponse (long pendingEvents,
                                    long publishedEvents,
                                    long deadEvents){


}
