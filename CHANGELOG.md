# Changelog

## [Unreleased]

### Added
- `GET /hearings`: search for enforcement hearings eligible for bulk resulting, sorted, grouped by hearing date and courtroom, and paged (100 rows), with the lead defendant's display fields, the full case detail, the caller's in-flight count and the "failed to share" flag
- `GET /config`: `maxBatchSize` and `searchWindowDays`
- `Problem` (RFC 9457) error body for 400, 401, 403, 422 (`TOO_MANY_RESULTS`), 502 and 503
