package repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.Homestay;
import repositories.interfaces.IHomestayRepository;
import utilities.AppConstants; // [UPDATE]
import utilities.TextFileHandler;

/**
 * Homestay Repository - Handles CRUD + Business Queries
 */
public class HomestayRepository extends TextFileHandler<Homestay> implements IHomestayRepository {

    private final List<Homestay> homestays = new ArrayList<>();

    // [UPDATE] Use AppConstants
    private final String FILE_NAME = AppConstants.FILE_HOMESTAYS;

    @Override
    public Homestay parseLine(String line) {
        if (line == null || line.trim().isEmpty())
            return null;

        try {
            // Logic handled "-" separated values as seen in original code
            String[] parts = line.split("-");
            if (parts.length >= 5) {
                String homeId = parts[0].trim();
                String homeName = parts[1].trim();
                int roomNumber = Integer.parseInt(parts[2].trim());

                // Address might contain dashes? Original logic re-assembled it:
                String address = parts[3].trim();
                if (parts.length > 5) {
                    for (int i = 4; i < parts.length - 1; i++) {
                        address += "-" + parts[i];
                    }
                }

                int maximumcapacity = Integer.parseInt(parts[parts.length - 1].trim());
                return new Homestay(homeId, homeName, roomNumber, address, maximumcapacity);
            }
        } catch (NumberFormatException e) {
            String sanitizedLine = line.replaceAll("[\r\n\t]", "_");
            System.err.println(">> Lỗi định dạng số ở dòng: " + sanitizedLine);
        } catch (Exception e) {
            String sanitizedLine = line.replaceAll("[\r\n\t]", "_");
            System.err.println(">> Bỏ qua dòng lỗi: " + sanitizedLine + " [" + e.getMessage() + "]");
        }
        return null;
    }

    @Override
    public List<Homestay> findAll() {
        return new ArrayList<>(homestays);
    }

    @Override
    public Homestay findById(String id) {
        return homestays.stream()
                .filter(h -> h.getHomeID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean save(Homestay entity) {
        if (exists(entity.getHomeID())) {
            return false;
        }
        return homestays.add(entity);
    }

    @Override
    public boolean update(Homestay entity) {
        Homestay existing = findById(entity.getHomeID());
        if (existing == null) {
            return false;
        }
        int index = homestays.indexOf(existing);
        homestays.set(index, entity);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return homestays.removeIf(h -> h.getHomeID().equalsIgnoreCase(id));
    }

    @Override
    public boolean exists(String id) {
        return findById(id) != null;
    }

    @Override
    public List<Homestay> findByName(String name) {
        return homestays.stream()
                .filter(h -> h.getHomeName().toLowerCase()
                        .contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void loadFromFile() {
        super.load(homestays, FILE_NAME);

        // [STRICT RULE] Ensure IDs are consistent if using auto-increment,
        // but Homestay ID seems to be String (manual entry?)
        // If needed, we would update ID generator state here.
    }
}
