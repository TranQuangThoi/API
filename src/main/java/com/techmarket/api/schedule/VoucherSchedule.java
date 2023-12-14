package com.techmarket.api.schedule;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.model.Voucher;
import com.techmarket.api.repository.VoucherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VoucherSchedule {
    @Autowired
    private VoucherRepository voucherRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void checkAndUpdateVoucherStatus() {
        Date currentDate = new Date();
        List<Voucher> vouchers = voucherRepository.findByExpiredBeforeAndStatusEquals(currentDate, UserBaseConstant.STATUS_ACTIVE);

        for (Voucher voucher : vouchers) {
            voucher.setStatus(UserBaseConstant.STATUS_LOCK);
            voucherRepository.save(voucher);
        }
    }
}
